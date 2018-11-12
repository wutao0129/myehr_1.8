package com.myehr.pojo.formmanage.form;

import java.util.ArrayList;
import java.util.List;

public class SysFormShowInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SysFormShowInfoExample() {
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
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andFormTypeIsNull() {
            addCriterion("FORM_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andFormTypeIsNotNull() {
            addCriterion("FORM_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andFormTypeEqualTo(String value) {
            addCriterion("FORM_TYPE =", value, "formType");
            return (Criteria) this;
        }

        public Criteria andFormTypeNotEqualTo(String value) {
            addCriterion("FORM_TYPE <>", value, "formType");
            return (Criteria) this;
        }

        public Criteria andFormTypeGreaterThan(String value) {
            addCriterion("FORM_TYPE >", value, "formType");
            return (Criteria) this;
        }

        public Criteria andFormTypeGreaterThanOrEqualTo(String value) {
            addCriterion("FORM_TYPE >=", value, "formType");
            return (Criteria) this;
        }

        public Criteria andFormTypeLessThan(String value) {
            addCriterion("FORM_TYPE <", value, "formType");
            return (Criteria) this;
        }

        public Criteria andFormTypeLessThanOrEqualTo(String value) {
            addCriterion("FORM_TYPE <=", value, "formType");
            return (Criteria) this;
        }

        public Criteria andFormTypeLike(String value) {
            addCriterion("FORM_TYPE like", value, "formType");
            return (Criteria) this;
        }

        public Criteria andFormTypeNotLike(String value) {
            addCriterion("FORM_TYPE not like", value, "formType");
            return (Criteria) this;
        }

        public Criteria andFormTypeIn(List<String> values) {
            addCriterion("FORM_TYPE in", values, "formType");
            return (Criteria) this;
        }

        public Criteria andFormTypeNotIn(List<String> values) {
            addCriterion("FORM_TYPE not in", values, "formType");
            return (Criteria) this;
        }

        public Criteria andFormTypeBetween(String value1, String value2) {
            addCriterion("FORM_TYPE between", value1, value2, "formType");
            return (Criteria) this;
        }

        public Criteria andFormTypeNotBetween(String value1, String value2) {
            addCriterion("FORM_TYPE not between", value1, value2, "formType");
            return (Criteria) this;
        }

        public Criteria andFormFunctionIsNull() {
            addCriterion("FORM_FUNCTION is null");
            return (Criteria) this;
        }

        public Criteria andFormFunctionIsNotNull() {
            addCriterion("FORM_FUNCTION is not null");
            return (Criteria) this;
        }

        public Criteria andFormFunctionEqualTo(String value) {
            addCriterion("FORM_FUNCTION =", value, "formFunction");
            return (Criteria) this;
        }

        public Criteria andFormFunctionNotEqualTo(String value) {
            addCriterion("FORM_FUNCTION <>", value, "formFunction");
            return (Criteria) this;
        }

        public Criteria andFormFunctionGreaterThan(String value) {
            addCriterion("FORM_FUNCTION >", value, "formFunction");
            return (Criteria) this;
        }

        public Criteria andFormFunctionGreaterThanOrEqualTo(String value) {
            addCriterion("FORM_FUNCTION >=", value, "formFunction");
            return (Criteria) this;
        }

        public Criteria andFormFunctionLessThan(String value) {
            addCriterion("FORM_FUNCTION <", value, "formFunction");
            return (Criteria) this;
        }

        public Criteria andFormFunctionLessThanOrEqualTo(String value) {
            addCriterion("FORM_FUNCTION <=", value, "formFunction");
            return (Criteria) this;
        }

        public Criteria andFormFunctionLike(String value) {
            addCriterion("FORM_FUNCTION like", value, "formFunction");
            return (Criteria) this;
        }

        public Criteria andFormFunctionNotLike(String value) {
            addCriterion("FORM_FUNCTION not like", value, "formFunction");
            return (Criteria) this;
        }

        public Criteria andFormFunctionIn(List<String> values) {
            addCriterion("FORM_FUNCTION in", values, "formFunction");
            return (Criteria) this;
        }

        public Criteria andFormFunctionNotIn(List<String> values) {
            addCriterion("FORM_FUNCTION not in", values, "formFunction");
            return (Criteria) this;
        }

        public Criteria andFormFunctionBetween(String value1, String value2) {
            addCriterion("FORM_FUNCTION between", value1, value2, "formFunction");
            return (Criteria) this;
        }

        public Criteria andFormFunctionNotBetween(String value1, String value2) {
            addCriterion("FORM_FUNCTION not between", value1, value2, "formFunction");
            return (Criteria) this;
        }

        public Criteria andFormPicIsNull() {
            addCriterion("FORM_PIC is null");
            return (Criteria) this;
        }

        public Criteria andFormPicIsNotNull() {
            addCriterion("FORM_PIC is not null");
            return (Criteria) this;
        }

        public Criteria andFormPicEqualTo(String value) {
            addCriterion("FORM_PIC =", value, "formPic");
            return (Criteria) this;
        }

        public Criteria andFormPicNotEqualTo(String value) {
            addCriterion("FORM_PIC <>", value, "formPic");
            return (Criteria) this;
        }

        public Criteria andFormPicGreaterThan(String value) {
            addCriterion("FORM_PIC >", value, "formPic");
            return (Criteria) this;
        }

        public Criteria andFormPicGreaterThanOrEqualTo(String value) {
            addCriterion("FORM_PIC >=", value, "formPic");
            return (Criteria) this;
        }

        public Criteria andFormPicLessThan(String value) {
            addCriterion("FORM_PIC <", value, "formPic");
            return (Criteria) this;
        }

        public Criteria andFormPicLessThanOrEqualTo(String value) {
            addCriterion("FORM_PIC <=", value, "formPic");
            return (Criteria) this;
        }

        public Criteria andFormPicLike(String value) {
            addCriterion("FORM_PIC like", value, "formPic");
            return (Criteria) this;
        }

        public Criteria andFormPicNotLike(String value) {
            addCriterion("FORM_PIC not like", value, "formPic");
            return (Criteria) this;
        }

        public Criteria andFormPicIn(List<String> values) {
            addCriterion("FORM_PIC in", values, "formPic");
            return (Criteria) this;
        }

        public Criteria andFormPicNotIn(List<String> values) {
            addCriterion("FORM_PIC not in", values, "formPic");
            return (Criteria) this;
        }

        public Criteria andFormPicBetween(String value1, String value2) {
            addCriterion("FORM_PIC between", value1, value2, "formPic");
            return (Criteria) this;
        }

        public Criteria andFormPicNotBetween(String value1, String value2) {
            addCriterion("FORM_PIC not between", value1, value2, "formPic");
            return (Criteria) this;
        }

        public Criteria andFormTagIsNull() {
            addCriterion("FORM_TAG is null");
            return (Criteria) this;
        }

        public Criteria andFormTagIsNotNull() {
            addCriterion("FORM_TAG is not null");
            return (Criteria) this;
        }

        public Criteria andFormTagEqualTo(String value) {
            addCriterion("FORM_TAG =", value, "formTag");
            return (Criteria) this;
        }

        public Criteria andFormTagNotEqualTo(String value) {
            addCriterion("FORM_TAG <>", value, "formTag");
            return (Criteria) this;
        }

        public Criteria andFormTagGreaterThan(String value) {
            addCriterion("FORM_TAG >", value, "formTag");
            return (Criteria) this;
        }

        public Criteria andFormTagGreaterThanOrEqualTo(String value) {
            addCriterion("FORM_TAG >=", value, "formTag");
            return (Criteria) this;
        }

        public Criteria andFormTagLessThan(String value) {
            addCriterion("FORM_TAG <", value, "formTag");
            return (Criteria) this;
        }

        public Criteria andFormTagLessThanOrEqualTo(String value) {
            addCriterion("FORM_TAG <=", value, "formTag");
            return (Criteria) this;
        }

        public Criteria andFormTagLike(String value) {
            addCriterion("FORM_TAG like", value, "formTag");
            return (Criteria) this;
        }

        public Criteria andFormTagNotLike(String value) {
            addCriterion("FORM_TAG not like", value, "formTag");
            return (Criteria) this;
        }

        public Criteria andFormTagIn(List<String> values) {
            addCriterion("FORM_TAG in", values, "formTag");
            return (Criteria) this;
        }

        public Criteria andFormTagNotIn(List<String> values) {
            addCriterion("FORM_TAG not in", values, "formTag");
            return (Criteria) this;
        }

        public Criteria andFormTagBetween(String value1, String value2) {
            addCriterion("FORM_TAG between", value1, value2, "formTag");
            return (Criteria) this;
        }

        public Criteria andFormTagNotBetween(String value1, String value2) {
            addCriterion("FORM_TAG not between", value1, value2, "formTag");
            return (Criteria) this;
        }

        public Criteria andFormTypeNameIsNull() {
            addCriterion("FORM_TYPE_NAME is null");
            return (Criteria) this;
        }

        public Criteria andFormTypeNameIsNotNull() {
            addCriterion("FORM_TYPE_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andFormTypeNameEqualTo(String value) {
            addCriterion("FORM_TYPE_NAME =", value, "formTypeName");
            return (Criteria) this;
        }

        public Criteria andFormTypeNameNotEqualTo(String value) {
            addCriterion("FORM_TYPE_NAME <>", value, "formTypeName");
            return (Criteria) this;
        }

        public Criteria andFormTypeNameGreaterThan(String value) {
            addCriterion("FORM_TYPE_NAME >", value, "formTypeName");
            return (Criteria) this;
        }

        public Criteria andFormTypeNameGreaterThanOrEqualTo(String value) {
            addCriterion("FORM_TYPE_NAME >=", value, "formTypeName");
            return (Criteria) this;
        }

        public Criteria andFormTypeNameLessThan(String value) {
            addCriterion("FORM_TYPE_NAME <", value, "formTypeName");
            return (Criteria) this;
        }

        public Criteria andFormTypeNameLessThanOrEqualTo(String value) {
            addCriterion("FORM_TYPE_NAME <=", value, "formTypeName");
            return (Criteria) this;
        }

        public Criteria andFormTypeNameLike(String value) {
            addCriterion("FORM_TYPE_NAME like", value, "formTypeName");
            return (Criteria) this;
        }

        public Criteria andFormTypeNameNotLike(String value) {
            addCriterion("FORM_TYPE_NAME not like", value, "formTypeName");
            return (Criteria) this;
        }

        public Criteria andFormTypeNameIn(List<String> values) {
            addCriterion("FORM_TYPE_NAME in", values, "formTypeName");
            return (Criteria) this;
        }

        public Criteria andFormTypeNameNotIn(List<String> values) {
            addCriterion("FORM_TYPE_NAME not in", values, "formTypeName");
            return (Criteria) this;
        }

        public Criteria andFormTypeNameBetween(String value1, String value2) {
            addCriterion("FORM_TYPE_NAME between", value1, value2, "formTypeName");
            return (Criteria) this;
        }

        public Criteria andFormTypeNameNotBetween(String value1, String value2) {
            addCriterion("FORM_TYPE_NAME not between", value1, value2, "formTypeName");
            return (Criteria) this;
        }

        public Criteria andFormTypeCodeIsNull() {
            addCriterion("FORM_TYPE_CODE is null");
            return (Criteria) this;
        }

        public Criteria andFormTypeCodeIsNotNull() {
            addCriterion("FORM_TYPE_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andFormTypeCodeEqualTo(String value) {
            addCriterion("FORM_TYPE_CODE =", value, "formTypeCode");
            return (Criteria) this;
        }

        public Criteria andFormTypeCodeNotEqualTo(String value) {
            addCriterion("FORM_TYPE_CODE <>", value, "formTypeCode");
            return (Criteria) this;
        }

        public Criteria andFormTypeCodeGreaterThan(String value) {
            addCriterion("FORM_TYPE_CODE >", value, "formTypeCode");
            return (Criteria) this;
        }

        public Criteria andFormTypeCodeGreaterThanOrEqualTo(String value) {
            addCriterion("FORM_TYPE_CODE >=", value, "formTypeCode");
            return (Criteria) this;
        }

        public Criteria andFormTypeCodeLessThan(String value) {
            addCriterion("FORM_TYPE_CODE <", value, "formTypeCode");
            return (Criteria) this;
        }

        public Criteria andFormTypeCodeLessThanOrEqualTo(String value) {
            addCriterion("FORM_TYPE_CODE <=", value, "formTypeCode");
            return (Criteria) this;
        }

        public Criteria andFormTypeCodeLike(String value) {
            addCriterion("FORM_TYPE_CODE like", value, "formTypeCode");
            return (Criteria) this;
        }

        public Criteria andFormTypeCodeNotLike(String value) {
            addCriterion("FORM_TYPE_CODE not like", value, "formTypeCode");
            return (Criteria) this;
        }

        public Criteria andFormTypeCodeIn(List<String> values) {
            addCriterion("FORM_TYPE_CODE in", values, "formTypeCode");
            return (Criteria) this;
        }

        public Criteria andFormTypeCodeNotIn(List<String> values) {
            addCriterion("FORM_TYPE_CODE not in", values, "formTypeCode");
            return (Criteria) this;
        }

        public Criteria andFormTypeCodeBetween(String value1, String value2) {
            addCriterion("FORM_TYPE_CODE between", value1, value2, "formTypeCode");
            return (Criteria) this;
        }

        public Criteria andFormTypeCodeNotBetween(String value1, String value2) {
            addCriterion("FORM_TYPE_CODE not between", value1, value2, "formTypeCode");
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