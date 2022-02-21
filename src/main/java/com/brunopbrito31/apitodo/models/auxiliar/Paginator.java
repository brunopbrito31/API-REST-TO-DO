package com.brunopbrito31.apitodo.models.auxiliar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paginator {

    private Integer pageNo;

    private Integer pageSize;

    private Long countItemsInDb;

    public Integer getStartLimit(){
        return this.pageNo * this.pageSize;
    }

    public Integer getQtPages(){
        Double qtPagesAux = (double) countItemsInDb / pageSize;
        if(qtPagesAux != qtPagesAux.intValue()){
            return qtPagesAux.intValue() + 1;
        }else{
            return qtPagesAux.intValue();
        }
    }
}
