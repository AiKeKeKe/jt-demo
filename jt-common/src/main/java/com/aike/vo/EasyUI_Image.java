package com.aike.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class EasyUI_Image {
    private Integer error = 0;
    private String url;
    private Integer width;
    private Integer height;
}
