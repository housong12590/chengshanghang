package com.jmm.csg.bean;


import java.util.List;

public class ProductReview {


    /**
     * code : 1
     * message : 请求成功
     * data : {"reviewCount":{"P":"4","B":"3","allP":"4","reviewCount":"7","allReviewCount":"7","allB":"3"},"review":[{"u_name":"你好高先1生","r_specification_id":2352824026989569,"r_coding":"091401","product_id":2352824026989568,"r_date":"2017-09-15 17:18:17","specification":"091401","r_status":"B","r_content":"我觉得不太好","u_sfid":"csh20170914/549404cf65bf4ce99c31819f5f0f4b46.jpg"},{"u_name":"微信用户-LpvFbk","r_specification_id":2352824026989569,"r_coding":"091401","product_id":2352824026989568,"r_date":"2017-09-14 15:36:57","specification":"091401","r_status":"B","r_content":"冒旭 客户经理 二维码 商品评价56789","u_sfid":""},{"u_name":"微信用户-LpvFbk","r_specification_id":2352824026989569,"r_coding":"091401","product_id":2352824026989568,"r_date":"2017-09-14 15:36:29","specification":"091401","r_status":"B","r_content":"冒旭 客户经理 二维码 商品评价","u_sfid":""},{"u_name":"微信用户-LpvFbk","r_specification_id":2352824026989569,"r_coding":"091401","product_id":2352824026989568,"r_date":"2017-09-14 15:30:54","specification":"091401","r_status":"P","r_content":"银行网点123456冒旭","u_sfid":""},{"u_name":"微信用户-LpvFbk","r_specification_id":2352824026989569,"r_coding":"091401","product_id":2352824026989568,"r_date":"2017-09-14 14:50:49","specification":"091401","r_status":"P","r_content":"冒旭123654789","u_sfid":""},{"u_name":"微信用户-LpvFbk","r_specification_id":2352824026989569,"r_coding":"091401","product_id":2352824026989568,"r_date":"2017-09-14 14:50:45","specification":"091401","r_status":"P","r_content":"冒旭123654789","u_sfid":""},{"u_name":"微信用户-LpvFbk","r_specification_id":2352824026989569,"r_coding":"091401","product_id":2352824026989568,"r_date":"2017-09-14 14:41:03","specification":"091401","r_status":"P","r_content":"冒旭？？，，！？。。","u_sfid":""}],"page":{"pageNo":1,"totalPage":1,"pageSize":10}}
     */

    private String code;
    private String message;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * reviewCount : {"P":"4","B":"3","allP":"4","reviewCount":"7","allReviewCount":"7","allB":"3"}
         * review : [{"u_name":"你好高先1生","r_specification_id":2352824026989569,"r_coding":"091401","product_id":2352824026989568,"r_date":"2017-09-15 17:18:17","specification":"091401","r_status":"B","r_content":"我觉得不太好","u_sfid":"csh20170914/549404cf65bf4ce99c31819f5f0f4b46.jpg"},{"u_name":"微信用户-LpvFbk","r_specification_id":2352824026989569,"r_coding":"091401","product_id":2352824026989568,"r_date":"2017-09-14 15:36:57","specification":"091401","r_status":"B","r_content":"冒旭 客户经理 二维码 商品评价56789","u_sfid":""},{"u_name":"微信用户-LpvFbk","r_specification_id":2352824026989569,"r_coding":"091401","product_id":2352824026989568,"r_date":"2017-09-14 15:36:29","specification":"091401","r_status":"B","r_content":"冒旭 客户经理 二维码 商品评价","u_sfid":""},{"u_name":"微信用户-LpvFbk","r_specification_id":2352824026989569,"r_coding":"091401","product_id":2352824026989568,"r_date":"2017-09-14 15:30:54","specification":"091401","r_status":"P","r_content":"银行网点123456冒旭","u_sfid":""},{"u_name":"微信用户-LpvFbk","r_specification_id":2352824026989569,"r_coding":"091401","product_id":2352824026989568,"r_date":"2017-09-14 14:50:49","specification":"091401","r_status":"P","r_content":"冒旭123654789","u_sfid":""},{"u_name":"微信用户-LpvFbk","r_specification_id":2352824026989569,"r_coding":"091401","product_id":2352824026989568,"r_date":"2017-09-14 14:50:45","specification":"091401","r_status":"P","r_content":"冒旭123654789","u_sfid":""},{"u_name":"微信用户-LpvFbk","r_specification_id":2352824026989569,"r_coding":"091401","product_id":2352824026989568,"r_date":"2017-09-14 14:41:03","specification":"091401","r_status":"P","r_content":"冒旭？？，，！？。。","u_sfid":""}]
         * page : {"pageNo":1,"totalPage":1,"pageSize":10}
         */

        private ReviewCountBean reviewCount;
        private PageBean page;
        private List<ReviewBean> review;

        public ReviewCountBean getReviewCount() {
            return reviewCount;
        }

        public void setReviewCount(ReviewCountBean reviewCount) {
            this.reviewCount = reviewCount;
        }

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public List<ReviewBean> getReview() {
            return review;
        }

        public void setReview(List<ReviewBean> review) {
            this.review = review;
        }

        public static class ReviewCountBean {
            /**
             * P : 4
             * B : 3
             * allP : 4
             * reviewCount : 7
             * allReviewCount : 7
             * allB : 3
             */

            private String P;
            private String B;
            private String allP;
            private String reviewCount;
            private String allReviewCount;
            private String allB;

            public String getP() {
                return P;
            }

            public void setP(String P) {
                this.P = P;
            }

            public String getB() {
                return B;
            }

            public void setB(String B) {
                this.B = B;
            }

            public String getAllP() {
                return allP;
            }

            public void setAllP(String allP) {
                this.allP = allP;
            }

            public String getReviewCount() {
                return reviewCount;
            }

            public void setReviewCount(String reviewCount) {
                this.reviewCount = reviewCount;
            }

            public String getAllReviewCount() {
                return allReviewCount;
            }

            public void setAllReviewCount(String allReviewCount) {
                this.allReviewCount = allReviewCount;
            }

            public String getAllB() {
                return allB;
            }

            public void setAllB(String allB) {
                this.allB = allB;
            }
        }

        public static class PageBean {
            /**
             * pageNo : 1
             * totalPage : 1
             * pageSize : 10
             */

            private int pageNo;
            private int totalPage;
            private int pageSize;

            public int getPageNo() {
                return pageNo;
            }

            public void setPageNo(int pageNo) {
                this.pageNo = pageNo;
            }

            public int getTotalPage() {
                return totalPage;
            }

            public void setTotalPage(int totalPage) {
                this.totalPage = totalPage;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }
        }

        public static class ReviewBean {
            /**
             * u_name : 你好高先1生
             * r_specification_id : 2352824026989569
             * r_coding : 091401
             * product_id : 2352824026989568
             * r_date : 2017-09-15 17:18:17
             * specification : 091401
             * r_status : B
             * r_content : 我觉得不太好
             * u_sfid : csh20170914/549404cf65bf4ce99c31819f5f0f4b46.jpg
             */

            private String u_name;
            private long r_specification_id;
            private String r_coding;
            private long product_id;
            private String r_date;
            private String specification;
            private String r_status;
            private String r_content;
            private String u_sfid;

            public String getU_name() {
                return u_name == null ? "" : u_name;
            }

            public void setU_name(String u_name) {
                this.u_name = u_name;
            }

            public long getR_specification_id() {
                return r_specification_id;
            }

            public void setR_specification_id(long r_specification_id) {
                this.r_specification_id = r_specification_id;
            }

            public String getR_coding() {
                return r_coding;
            }

            public void setR_coding(String r_coding) {
                this.r_coding = r_coding;
            }

            public long getProduct_id() {
                return product_id;
            }

            public void setProduct_id(long product_id) {
                this.product_id = product_id;
            }

            public String getR_date() {
                return r_date;
            }

            public void setR_date(String r_date) {
                this.r_date = r_date;
            }

            public String getSpecification() {
                return specification;
            }

            public void setSpecification(String specification) {
                this.specification = specification;
            }

            public String getR_status() {
                return r_status;
            }

            public void setR_status(String r_status) {
                this.r_status = r_status;
            }

            public String getR_content() {
                return r_content;
            }

            public void setR_content(String r_content) {
                this.r_content = r_content;
            }

            public String getU_sfid() {
                return u_sfid;
            }

            public void setU_sfid(String u_sfid) {
                this.u_sfid = u_sfid;
            }
        }
    }
}
