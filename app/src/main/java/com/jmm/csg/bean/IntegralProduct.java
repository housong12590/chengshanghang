package com.jmm.csg.bean;

import java.util.List;


public class IntegralProduct {

    private String code;
    private List<Entity> data;

    public String getCode() {
        return code;
    }

    public List<Entity> getData() {
        return data;
    }

    public static class Entity {
        private String PRODUCT_NAME;
        private String INTEGRAL_NUM;
        private String ID;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getPRODUCT_NAME() {
            return PRODUCT_NAME;
        }

        public String getINTEGRAL_NUM() {
            return INTEGRAL_NUM;
        }

        public String getID() {
            return ID;
        }
    }
}
