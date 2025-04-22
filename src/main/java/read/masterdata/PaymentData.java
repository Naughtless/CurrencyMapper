package main.java.read.masterdata;

import main.java.common.CSV;

import java.util.Objects;

public class PaymentData implements CSV {
    //<editor-fold desc="Properties">
    private static String header = "site|product_id|currency|legal_entity|studentid|fullname|student_status|paymentid|payment_type|item_type|payment_status|amount|gst|payment_date|module_start_date|module_end_date|schedule|enrolment_date|Addon";

    private String site;
    private String productId;
    private String currency;
    private String legalEntity;
    private String studentId;
    private String fullName;
    private String studentStatus;
    private String paymentId;
    private String paymentType;
    private String itemType;
    private String paymentStatus;
    private String amount;
    private String gst;
    private String paymentDate;
    private String moduleStartDate;
    private String moduleEndDate;
    private String schedule;
    private String enrolmentDate;
    private String addon;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public PaymentData() {}

    public PaymentData(
            String site,
            String productId,
            String currency,
            String legalEntity,
            String studentId,
            String fullName,
            String studentStatus,
            String paymentId,
            String paymentType,
            String itemType,
            String paymentStatus,
            String amount,
            String gst,
            String paymentDate,
            String moduleStartDate,
            String moduleEndDate,
            String schedule,
            String enrolmentDate,
            String addon
    ) {
        this.site = site;
        this.productId = productId;
        this.currency = currency;
        this.legalEntity = legalEntity;
        this.studentId = studentId;
        this.fullName = fullName;
        this.studentStatus = studentStatus;
        this.paymentId = paymentId;
        this.paymentType = paymentType;
        this.itemType = itemType;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.gst = gst;
        this.paymentDate = paymentDate;
        this.moduleStartDate = moduleStartDate;
        this.moduleEndDate = moduleEndDate;
        this.schedule = schedule;
        this.enrolmentDate = enrolmentDate;
        this.addon = addon;
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public String buildCSVLine() {
        StringBuilder sb = new StringBuilder();

        sb.append(site).append("|");
        sb.append(productId).append("|");
        sb.append(currency).append("|");
        sb.append(legalEntity).append("|");
        sb.append(studentId).append("|");
        sb.append(fullName).append("|");
        sb.append(studentStatus).append("|");
        sb.append(paymentId).append("|");
        sb.append(paymentType).append("|");
        sb.append(itemType).append("|");
        sb.append(paymentStatus).append("|");
        sb.append(amount).append("|");
        sb.append(gst).append("|");
        sb.append(paymentDate).append("|");
        sb.append(moduleStartDate).append("|");
        sb.append(moduleEndDate).append("|");
        sb.append(schedule).append("|");
        sb.append(enrolmentDate).append("|");
        sb.append(addon);

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentData that = (PaymentData) o;
        return Objects.equals(site, that.site) && Objects.equals(productId, that.productId) && Objects.equals(currency, that.currency) && Objects.equals(legalEntity, that.legalEntity) && Objects.equals(studentId, that.studentId) && Objects.equals(fullName, that.fullName) && Objects.equals(studentStatus, that.studentStatus) && Objects.equals(paymentId, that.paymentId) && Objects.equals(paymentType, that.paymentType) && Objects.equals(itemType, that.itemType) && Objects.equals(paymentStatus, that.paymentStatus) && Objects.equals(amount, that.amount) && Objects.equals(gst, that.gst) && Objects.equals(paymentDate, that.paymentDate) && Objects.equals(moduleStartDate, that.moduleStartDate) && Objects.equals(moduleEndDate, that.moduleEndDate) && Objects.equals(schedule, that.schedule) && Objects.equals(enrolmentDate, that.enrolmentDate) && Objects.equals(addon, that.addon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(site, productId, currency, legalEntity, studentId, fullName, studentStatus, paymentId, paymentType, itemType, paymentStatus, amount, gst, paymentDate, moduleStartDate, moduleEndDate, schedule, enrolmentDate, addon);
    }

    @Override
    public String toString() {
        return "PaymentData\n{" +
                "\nsite='" + site + '\'' +
                "\nproductId='" + productId + '\'' +
                "\ncurrency='" + currency + '\'' +
                "\nlegalEntity='" + legalEntity + '\'' +
                "\nstudentId='" + studentId + '\'' +
                "\nfullName='" + fullName + '\'' +
                "\nstudentStatus='" + studentStatus + '\'' +
                "\npaymentId='" + paymentId + '\'' +
                "\npaymentType='" + paymentType + '\'' +
                "\nitemType='" + itemType + '\'' +
                "\npaymentStatus='" + paymentStatus + '\'' +
                "\namount='" + amount + '\'' +
                "\ngst='" + gst + '\'' +
                "\npaymentDate='" + paymentDate + '\'' +
                "\nmoduleStartDate='" + moduleStartDate + '\'' +
                "\nmoduleEndDate='" + moduleEndDate + '\'' +
                "\nschedule='" + schedule + '\'' +
                "\nenrolmentDate='" + enrolmentDate + '\'' +
                "\naddon='" + addon + '\'' +
                "\n}";
    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public static String getHeader() {
        return header;
    }

    public static void setHeader(String header) {
        PaymentData.header = header;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(String legalEntity) {
        this.legalEntity = legalEntity;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getModuleStartDate() {
        return moduleStartDate;
    }

    public void setModuleStartDate(String moduleStartDate) {
        this.moduleStartDate = moduleStartDate;
    }

    public String getModuleEndDate() {
        return moduleEndDate;
    }

    public void setModuleEndDate(String moduleEndDate) {
        this.moduleEndDate = moduleEndDate;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getEnrolmentDate() {
        return enrolmentDate;
    }

    public void setEnrolmentDate(String enrolmentDate) {
        this.enrolmentDate = enrolmentDate;
    }

    public String getAddon() {
        return addon;
    }

    public void setAddon(String addon) {
        this.addon = addon;
    }
    //</editor-fold>
}
