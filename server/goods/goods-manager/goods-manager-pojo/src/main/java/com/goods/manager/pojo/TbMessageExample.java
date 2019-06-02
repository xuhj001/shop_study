package com.goods.manager.pojo;

import java.util.ArrayList;
import java.util.List;

public class TbMessageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbMessageExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andCountIsNull() {
            addCriterion("count is null");
            return (Criteria) this;
        }

        public Criteria andCountIsNotNull() {
            addCriterion("count is not null");
            return (Criteria) this;
        }

        public Criteria andCountEqualTo(Integer value) {
            addCriterion("count =", value, "count");
            return (Criteria) this;
        }

        public Criteria andCountNotEqualTo(Integer value) {
            addCriterion("count <>", value, "count");
            return (Criteria) this;
        }

        public Criteria andCountGreaterThan(Integer value) {
            addCriterion("count >", value, "count");
            return (Criteria) this;
        }

        public Criteria andCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("count >=", value, "count");
            return (Criteria) this;
        }

        public Criteria andCountLessThan(Integer value) {
            addCriterion("count <", value, "count");
            return (Criteria) this;
        }

        public Criteria andCountLessThanOrEqualTo(Integer value) {
            addCriterion("count <=", value, "count");
            return (Criteria) this;
        }

        public Criteria andCountIn(List<Integer> values) {
            addCriterion("count in", values, "count");
            return (Criteria) this;
        }

        public Criteria andCountNotIn(List<Integer> values) {
            addCriterion("count not in", values, "count");
            return (Criteria) this;
        }

        public Criteria andCountBetween(Integer value1, Integer value2) {
            addCriterion("count between", value1, value2, "count");
            return (Criteria) this;
        }

        public Criteria andCountNotBetween(Integer value1, Integer value2) {
            addCriterion("count not between", value1, value2, "count");
            return (Criteria) this;
        }

        public Criteria andOrdernumIsNull() {
            addCriterion("ordernum is null");
            return (Criteria) this;
        }

        public Criteria andOrdernumIsNotNull() {
            addCriterion("ordernum is not null");
            return (Criteria) this;
        }

        public Criteria andOrdernumEqualTo(Integer value) {
            addCriterion("ordernum =", value, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumNotEqualTo(Integer value) {
            addCriterion("ordernum <>", value, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumGreaterThan(Integer value) {
            addCriterion("ordernum >", value, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumGreaterThanOrEqualTo(Integer value) {
            addCriterion("ordernum >=", value, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumLessThan(Integer value) {
            addCriterion("ordernum <", value, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumLessThanOrEqualTo(Integer value) {
            addCriterion("ordernum <=", value, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumIn(List<Integer> values) {
            addCriterion("ordernum in", values, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumNotIn(List<Integer> values) {
            addCriterion("ordernum not in", values, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumBetween(Integer value1, Integer value2) {
            addCriterion("ordernum between", value1, value2, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumNotBetween(Integer value1, Integer value2) {
            addCriterion("ordernum not between", value1, value2, "ordernum");
            return (Criteria) this;
        }

        public Criteria andCommentsnumIsNull() {
            addCriterion("commentsnum is null");
            return (Criteria) this;
        }

        public Criteria andCommentsnumIsNotNull() {
            addCriterion("commentsnum is not null");
            return (Criteria) this;
        }

        public Criteria andCommentsnumEqualTo(Integer value) {
            addCriterion("commentsnum =", value, "commentsnum");
            return (Criteria) this;
        }

        public Criteria andCommentsnumNotEqualTo(Integer value) {
            addCriterion("commentsnum <>", value, "commentsnum");
            return (Criteria) this;
        }

        public Criteria andCommentsnumGreaterThan(Integer value) {
            addCriterion("commentsnum >", value, "commentsnum");
            return (Criteria) this;
        }

        public Criteria andCommentsnumGreaterThanOrEqualTo(Integer value) {
            addCriterion("commentsnum >=", value, "commentsnum");
            return (Criteria) this;
        }

        public Criteria andCommentsnumLessThan(Integer value) {
            addCriterion("commentsnum <", value, "commentsnum");
            return (Criteria) this;
        }

        public Criteria andCommentsnumLessThanOrEqualTo(Integer value) {
            addCriterion("commentsnum <=", value, "commentsnum");
            return (Criteria) this;
        }

        public Criteria andCommentsnumIn(List<Integer> values) {
            addCriterion("commentsnum in", values, "commentsnum");
            return (Criteria) this;
        }

        public Criteria andCommentsnumNotIn(List<Integer> values) {
            addCriterion("commentsnum not in", values, "commentsnum");
            return (Criteria) this;
        }

        public Criteria andCommentsnumBetween(Integer value1, Integer value2) {
            addCriterion("commentsnum between", value1, value2, "commentsnum");
            return (Criteria) this;
        }

        public Criteria andCommentsnumNotBetween(Integer value1, Integer value2) {
            addCriterion("commentsnum not between", value1, value2, "commentsnum");
            return (Criteria) this;
        }

        public Criteria andReplynumIsNull() {
            addCriterion("replynum is null");
            return (Criteria) this;
        }

        public Criteria andReplynumIsNotNull() {
            addCriterion("replynum is not null");
            return (Criteria) this;
        }

        public Criteria andReplynumEqualTo(Integer value) {
            addCriterion("replynum =", value, "replynum");
            return (Criteria) this;
        }

        public Criteria andReplynumNotEqualTo(Integer value) {
            addCriterion("replynum <>", value, "replynum");
            return (Criteria) this;
        }

        public Criteria andReplynumGreaterThan(Integer value) {
            addCriterion("replynum >", value, "replynum");
            return (Criteria) this;
        }

        public Criteria andReplynumGreaterThanOrEqualTo(Integer value) {
            addCriterion("replynum >=", value, "replynum");
            return (Criteria) this;
        }

        public Criteria andReplynumLessThan(Integer value) {
            addCriterion("replynum <", value, "replynum");
            return (Criteria) this;
        }

        public Criteria andReplynumLessThanOrEqualTo(Integer value) {
            addCriterion("replynum <=", value, "replynum");
            return (Criteria) this;
        }

        public Criteria andReplynumIn(List<Integer> values) {
            addCriterion("replynum in", values, "replynum");
            return (Criteria) this;
        }

        public Criteria andReplynumNotIn(List<Integer> values) {
            addCriterion("replynum not in", values, "replynum");
            return (Criteria) this;
        }

        public Criteria andReplynumBetween(Integer value1, Integer value2) {
            addCriterion("replynum between", value1, value2, "replynum");
            return (Criteria) this;
        }

        public Criteria andReplynumNotBetween(Integer value1, Integer value2) {
            addCriterion("replynum not between", value1, value2, "replynum");
            return (Criteria) this;
        }

        public Criteria andMuseridIsNull() {
            addCriterion("muserId is null");
            return (Criteria) this;
        }

        public Criteria andMuseridIsNotNull() {
            addCriterion("muserId is not null");
            return (Criteria) this;
        }

        public Criteria andMuseridEqualTo(Long value) {
            addCriterion("muserId =", value, "muserid");
            return (Criteria) this;
        }

        public Criteria andMuseridNotEqualTo(Long value) {
            addCriterion("muserId <>", value, "muserid");
            return (Criteria) this;
        }

        public Criteria andMuseridGreaterThan(Long value) {
            addCriterion("muserId >", value, "muserid");
            return (Criteria) this;
        }

        public Criteria andMuseridGreaterThanOrEqualTo(Long value) {
            addCriterion("muserId >=", value, "muserid");
            return (Criteria) this;
        }

        public Criteria andMuseridLessThan(Long value) {
            addCriterion("muserId <", value, "muserid");
            return (Criteria) this;
        }

        public Criteria andMuseridLessThanOrEqualTo(Long value) {
            addCriterion("muserId <=", value, "muserid");
            return (Criteria) this;
        }

        public Criteria andMuseridIn(List<Long> values) {
            addCriterion("muserId in", values, "muserid");
            return (Criteria) this;
        }

        public Criteria andMuseridNotIn(List<Long> values) {
            addCriterion("muserId not in", values, "muserid");
            return (Criteria) this;
        }

        public Criteria andMuseridBetween(Long value1, Long value2) {
            addCriterion("muserId between", value1, value2, "muserid");
            return (Criteria) this;
        }

        public Criteria andMuseridNotBetween(Long value1, Long value2) {
            addCriterion("muserId not between", value1, value2, "muserid");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}