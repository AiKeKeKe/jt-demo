package com.aike.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 公共的pojo
 */
@Data
@Accessors(chain = true)
public class BasePojo implements Serializable {
    private Date created;
    private Date updated;
}
