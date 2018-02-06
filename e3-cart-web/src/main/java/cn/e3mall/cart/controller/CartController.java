package cn.e3mall.cart.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车Controller
 * Created by cxq on 2018/1/30.
 */
@Controller
public class CartController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CartService cartService;

    @Value("${COOKIE_CART_EXPIRE}")
    private Integer COOKIE_CART_EXPIRE;

    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCart(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
        TbUser user = (TbUser) request.getAttribute("user");
        if(user != null) {
            cartService.deleteCart(user.getId(), itemId);
            return "redirect:/cart/cart.html";
        }
        // 1、从url中取商品id
        // 2、从cookie中取购物车商品列表
        List<TbItem> cartList = getCartListFromCookie(request);
        // 3、遍历列表找到对应的商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()) {
                // 4、删除商品。
                cartList.remove(tbItem);
                break;
            }
        }
        // 5、把商品列表写入cookie。
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
        // 6、返回逻辑视图：在逻辑视图中做redirect跳转。
        return "redirect:/cart/cart.html";
    }

    /**
     * 更新cookie购物车信息
     *
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateItemNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request, HttpServletResponse response) {

        TbUser user = (TbUser) request.getAttribute("user");
        if(user != null) return cartService.updateCart(user.getId(), itemId, num);
        // 1、接收两个参数
        // 2、从cookie中取商品列表
        List<TbItem> cartList = getCartListFromCookie(request);
        // 3、遍历商品列表找到对应商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()) {
                // 4、更新商品数量
                tbItem.setNum(num);
                break;
            }
        }
        // 5、把商品列表写入cookie。
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
        // 6、响应e3Result。Json数据。
        return E3Result.ok();
    }


    /**
     * 购物车列表
     *
     * @param req
     * @return
     */
    @RequestMapping("/cart/cart")
    public String showCart(HttpServletRequest req, HttpServletResponse resp) {
        //判断用户是否为登录状态
        TbUser tbUser = (TbUser) req.getAttribute("user");

        List<TbItem> cartList = getCartListFromCookie(req);
        if (tbUser != null) {
            long userId = tbUser.getId();
            //有用户信息，合并cookie中的商品信息，并删除cookie的购物车信息；
            cartService.mergeCart(userId, cartList);
            CookieUtils.deleteCookie(req, resp, "cart");
            //从服务端取购物车列表
            cartList = cartService.getCartList(userId);

        }

        //从cookie中取购物车列表 把列表返回给页面
        req.setAttribute("cartList", cartList);
        //返回逻辑视图
        return "cart";
    }

    /*
     * 添加购物车
     * @param itemId
     * @param num
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping("/cart/add/{itemId}")
    public String cartAdd(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num, HttpServletRequest req, HttpServletResponse resp) {
        //判断用户是否为登录状态
        TbUser user = (TbUser) req.getAttribute("user");
        if (user != null) {
            //保存购物车到服务端
            cartService.addCart(user.getId(), itemId, num);
            return "cartSuccess";
        }
        // 1、从cookie中查询商品列表。
        List<TbItem> cartList = getCartListFromCookie(req);
        // 2、判断商品在商品列表中是否存在。
        boolean isItemExist = false;

        for (TbItem tbItem : cartList) {
            //对象比较的是地址，应该是值的比较
            if (tbItem.getId() == itemId.longValue()) {
                // 3、如果存在，商品数量相加。
                tbItem.setNum(tbItem.getNum() + num);
                isItemExist = true;
                break;
            }
        }
        if (!isItemExist) {
            // 4、不存在，根据商品id查询商品信息。
            TbItem tbItem = itemService.getItemById(itemId);
            String image = tbItem.getImage();
            //取一张图片
            if (StringUtils.isNoneBlank(image)) {
                String[] images = image.split(",");
                tbItem.setImage(images[0]);
            }
            //设置购买商品数量
            tbItem.setNum(num);
            // 5、把商品添加到购车列表。
            cartList.add(tbItem);
        }
        // 6、把购车商品列表写入cookie。
        CookieUtils.setCookie(req, resp, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
        return "cartSuccess";
    }

    /**
     * 从cookie中取购物车列表
     *
     * @param request
     * @return
     */
    private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
        //取购物车列表
        String json = CookieUtils.getCookieValue(request, "cart", true);
        //判断json是否为null
        if (StringUtils.isNotBlank(json)) {
            //把json转换成商品列表返回
            List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
            return list;
        }
        return new ArrayList<>();
    }

}
