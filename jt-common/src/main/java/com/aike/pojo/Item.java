package com.aike.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品表实体类
 */
@Data
@TableName("tb_item")
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item extends BasePojo{
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String sellPoint;
    private Long price;
    private Integer num;
    private String barcode;
    private String image;
    private Long cid;
    private Integer status;

    public String[] getImages() {
        return image.split(",");
    }
}
