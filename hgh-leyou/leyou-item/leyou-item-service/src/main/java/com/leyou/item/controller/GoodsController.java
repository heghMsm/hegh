package com.leyou.item.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 根据条件分页查询Spu
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",required = false)Integer rows
    ){
        PageResult<SpuBo> result = this.goodsService.querySpuPage(key, saleable, page, rows);
        if (result == null || CollectionUtils.isEmpty(result.getItems())){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
