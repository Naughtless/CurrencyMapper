package main.java.read.externaldata;

import main.java.common.CSV;

import java.util.Objects;

public class PayPal implements CSV {
    //<editor-fold desc="Properties">
    private static String header = "Date|Time|Time zone|Name|Type|Status|Currency|Gross|Fee|Net|From email|To email|Transaction ID|Counterparty Status|Address status|GST|Reference Txn ID|Invoice Number|Receipt ID|Balance|Contact Phone Number|Subject|Note|Payment method|Card Type|Transaction Event Code|Payment Tracking ID|Bank Reference ID|Buyer Country Code|Vouchers|Special Offers|Loyalty Card Number|Balance Impact|Buyer Wallet|Tip|Discount|Seller ID|Risk Filter|Credit Transactional Fee|Credit Promotional Fee|Credit Term|Credit Offer Type|Original Invoice ID|Payment Source Subtype|Campaign Fee|Campaign Name|Campaign Discount|Campaign Discount Currency";

    private String date;
    private String time;
    private String timezone;
    private String name;
    private String type;
    private String status;
    private String currency;
    private String gross;
    private String fee;
    private String net;
    private String fromEmail;
    private String toEmail;
    private String transactionId;
    private String counterpartyStatus;
    private String addressStatus;
    private String gst;
    private String referenceTxnId;
    private String invoiceNumber;
    private String receiptId;
    private String balance;
    private String contactPhoneNumber;
    private String subject;
    private String note;
    private String paymentMethod;
    private String cardType;
    private String transactionEventCode;
    private String paymentTrackingId;
    private String bankReferenceId;
    private String buyerCountryCode;
    private String vouchers;
    private String specialOffers;
    private String loyaltyCardNumber;
    private String balanceImpact;
    private String buyerWallet;
    private String tip;
    private String discount;
    private String sellerId;
    private String riskFilter;
    private String creditTransactionalFee;
    private String creditPromotionalFee;
    private String creditTerm;
    private String creditOfferType;
    private String originalInvoiceId;
    private String paymentSourceSubtype;
    private String campaignFee;
    private String campaignName;
    private String campaignDiscount;
    private String campaignDiscountCurrency;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public PayPal() {}

    public PayPal(String date, String time, String timezone, String name, String type, String status, String currency, String gross, String fee, String net, String fromEmail, String toEmail, String transactionId, String counterpartyStatus, String addressStatus, String gst, String referenceTxnId, String invoiceNumber, String receiptId, String balance, String contactPhoneNumber, String subject, String note, String paymentMethod, String cardType, String transactionEventCode, String paymentTrackingId, String bankReferenceId, String buyerCountryCode, String vouchers, String specialOffers, String loyaltyCardNumber, String balanceImpact, String buyerWallet, String tip, String discount, String sellerId, String riskFilter, String creditTransactionalFee, String creditPromotionalFee, String creditTerm, String creditOfferType, String originalInvoiceId, String paymentSourceSubtype, String campaignFee, String campaignName, String campaignDiscount, String campaignDiscountCurrency) {
        this.date = date;
        this.time = time;
        this.timezone = timezone;
        this.name = name;
        this.type = type;
        this.status = status;
        this.currency = currency;
        this.gross = gross;
        this.fee = fee;
        this.net = net;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.transactionId = transactionId;
        this.counterpartyStatus = counterpartyStatus;
        this.addressStatus = addressStatus;
        this.gst = gst;
        this.referenceTxnId = referenceTxnId;
        this.invoiceNumber = invoiceNumber;
        this.receiptId = receiptId;
        this.balance = balance;
        this.contactPhoneNumber = contactPhoneNumber;
        this.subject = subject;
        this.note = note;
        this.paymentMethod = paymentMethod;
        this.cardType = cardType;
        this.transactionEventCode = transactionEventCode;
        this.paymentTrackingId = paymentTrackingId;
        this.bankReferenceId = bankReferenceId;
        this.buyerCountryCode = buyerCountryCode;
        this.vouchers = vouchers;
        this.specialOffers = specialOffers;
        this.loyaltyCardNumber = loyaltyCardNumber;
        this.balanceImpact = balanceImpact;
        this.buyerWallet = buyerWallet;
        this.tip = tip;
        this.discount = discount;
        this.sellerId = sellerId;
        this.riskFilter = riskFilter;
        this.creditTransactionalFee = creditTransactionalFee;
        this.creditPromotionalFee = creditPromotionalFee;
        this.creditTerm = creditTerm;
        this.creditOfferType = creditOfferType;
        this.originalInvoiceId = originalInvoiceId;
        this.paymentSourceSubtype = paymentSourceSubtype;
        this.campaignFee = campaignFee;
        this.campaignName = campaignName;
        this.campaignDiscount = campaignDiscount;
        this.campaignDiscountCurrency = campaignDiscountCurrency;
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public String buildCSVLine() {
        StringBuilder sb = new StringBuilder();

        sb.append(date).append("|");
        sb.append(time).append("|");
        sb.append(timezone).append("|");
        sb.append(name).append("|");
        sb.append(type).append("|");
        sb.append(status).append("|");
        sb.append(currency).append("|");
        sb.append(gross).append("|");
        sb.append(fee).append("|");
        sb.append(net).append("|");
        sb.append(fromEmail).append("|");
        sb.append(toEmail).append("|");
        sb.append(transactionId).append("|");
        sb.append(counterpartyStatus).append("|");
        sb.append(addressStatus).append("|");
        sb.append(gst).append("|");
        sb.append(referenceTxnId).append("|");
        sb.append(invoiceNumber).append("|");
        sb.append(receiptId).append("|");
        sb.append(balance).append("|");
        sb.append(contactPhoneNumber).append("|");
        sb.append(subject).append("|");
        sb.append(note).append("|");
        sb.append(paymentMethod).append("|");
        sb.append(cardType).append("|");
        sb.append(transactionEventCode).append("|");
        sb.append(paymentTrackingId).append("|");
        sb.append(bankReferenceId).append("|");
        sb.append(buyerCountryCode).append("|");
        sb.append(vouchers).append("|");
        sb.append(specialOffers).append("|");
        sb.append(loyaltyCardNumber).append("|");
        sb.append(balanceImpact).append("|");
        sb.append(buyerWallet).append("|");
        sb.append(tip).append("|");
        sb.append(discount).append("|");
        sb.append(sellerId).append("|");
        sb.append(riskFilter).append("|");
        sb.append(creditTransactionalFee).append("|");
        sb.append(creditPromotionalFee).append("|");
        sb.append(creditTerm).append("|");
        sb.append(creditOfferType).append("|");
        sb.append(originalInvoiceId).append("|");
        sb.append(paymentSourceSubtype).append("|");
        sb.append(campaignFee).append("|");
        sb.append(campaignName).append("|");
        sb.append(campaignDiscount).append("|");
        sb.append(campaignDiscountCurrency);

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayPal payPal = (PayPal) o;
        return Objects.equals(date, payPal.date) && Objects.equals(time, payPal.time) && Objects.equals(timezone, payPal.timezone) && Objects.equals(name, payPal.name) && Objects.equals(type, payPal.type) && Objects.equals(status, payPal.status) && Objects.equals(currency, payPal.currency) && Objects.equals(gross, payPal.gross) && Objects.equals(fee, payPal.fee) && Objects.equals(net, payPal.net) && Objects.equals(fromEmail, payPal.fromEmail) && Objects.equals(toEmail, payPal.toEmail) && Objects.equals(transactionId, payPal.transactionId) && Objects.equals(counterpartyStatus, payPal.counterpartyStatus) && Objects.equals(addressStatus, payPal.addressStatus) && Objects.equals(gst, payPal.gst) && Objects.equals(referenceTxnId, payPal.referenceTxnId) && Objects.equals(invoiceNumber, payPal.invoiceNumber) && Objects.equals(receiptId, payPal.receiptId) && Objects.equals(balance, payPal.balance) && Objects.equals(contactPhoneNumber, payPal.contactPhoneNumber) && Objects.equals(subject, payPal.subject) && Objects.equals(note, payPal.note) && Objects.equals(paymentMethod, payPal.paymentMethod) && Objects.equals(cardType, payPal.cardType) && Objects.equals(transactionEventCode, payPal.transactionEventCode) && Objects.equals(paymentTrackingId, payPal.paymentTrackingId) && Objects.equals(bankReferenceId, payPal.bankReferenceId) && Objects.equals(buyerCountryCode, payPal.buyerCountryCode) && Objects.equals(vouchers, payPal.vouchers) && Objects.equals(specialOffers, payPal.specialOffers) && Objects.equals(loyaltyCardNumber, payPal.loyaltyCardNumber) && Objects.equals(balanceImpact, payPal.balanceImpact) && Objects.equals(buyerWallet, payPal.buyerWallet) && Objects.equals(tip, payPal.tip) && Objects.equals(discount, payPal.discount) && Objects.equals(sellerId, payPal.sellerId) && Objects.equals(riskFilter, payPal.riskFilter) && Objects.equals(creditTransactionalFee, payPal.creditTransactionalFee) && Objects.equals(creditPromotionalFee, payPal.creditPromotionalFee) && Objects.equals(creditTerm, payPal.creditTerm) && Objects.equals(creditOfferType, payPal.creditOfferType) && Objects.equals(originalInvoiceId, payPal.originalInvoiceId) && Objects.equals(paymentSourceSubtype, payPal.paymentSourceSubtype) && Objects.equals(campaignFee, payPal.campaignFee) && Objects.equals(campaignName, payPal.campaignName) && Objects.equals(campaignDiscount, payPal.campaignDiscount) && Objects.equals(campaignDiscountCurrency, payPal.campaignDiscountCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, timezone, name, type, status, currency, gross, fee, net, fromEmail, toEmail, transactionId, counterpartyStatus, addressStatus, gst, referenceTxnId, invoiceNumber, receiptId, balance, contactPhoneNumber, subject, note, paymentMethod, cardType, transactionEventCode, paymentTrackingId, bankReferenceId, buyerCountryCode, vouchers, specialOffers, loyaltyCardNumber, balanceImpact, buyerWallet, tip, discount, sellerId, riskFilter, creditTransactionalFee, creditPromotionalFee, creditTerm, creditOfferType, originalInvoiceId, paymentSourceSubtype, campaignFee, campaignName, campaignDiscount, campaignDiscountCurrency);
    }

    @Override
    public String toString() {
        return buildCSVLine();
    }

    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public static String getHeader() {
        return header;
    }

    public static void setHeader(String header) {
        PayPal.header = header;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCounterpartyStatus() {
        return counterpartyStatus;
    }

    public void setCounterpartyStatus(String counterpartyStatus) {
        this.counterpartyStatus = counterpartyStatus;
    }

    public String getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(String addressStatus) {
        this.addressStatus = addressStatus;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getReferenceTxnId() {
        return referenceTxnId;
    }

    public void setReferenceTxnId(String referenceTxnId) {
        this.referenceTxnId = referenceTxnId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getTransactionEventCode() {
        return transactionEventCode;
    }

    public void setTransactionEventCode(String transactionEventCode) {
        this.transactionEventCode = transactionEventCode;
    }

    public String getPaymentTrackingId() {
        return paymentTrackingId;
    }

    public void setPaymentTrackingId(String paymentTrackingId) {
        this.paymentTrackingId = paymentTrackingId;
    }

    public String getBankReferenceId() {
        return bankReferenceId;
    }

    public void setBankReferenceId(String bankReferenceId) {
        this.bankReferenceId = bankReferenceId;
    }

    public String getBuyerCountryCode() {
        return buyerCountryCode;
    }

    public void setBuyerCountryCode(String buyerCountryCode) {
        this.buyerCountryCode = buyerCountryCode;
    }

    public String getVouchers() {
        return vouchers;
    }

    public void setVouchers(String vouchers) {
        this.vouchers = vouchers;
    }

    public String getSpecialOffers() {
        return specialOffers;
    }

    public void setSpecialOffers(String specialOffers) {
        this.specialOffers = specialOffers;
    }

    public String getLoyaltyCardNumber() {
        return loyaltyCardNumber;
    }

    public void setLoyaltyCardNumber(String loyaltyCardNumber) {
        this.loyaltyCardNumber = loyaltyCardNumber;
    }

    public String getBalanceImpact() {
        return balanceImpact;
    }

    public void setBalanceImpact(String balanceImpact) {
        this.balanceImpact = balanceImpact;
    }

    public String getBuyerWallet() {
        return buyerWallet;
    }

    public void setBuyerWallet(String buyerWallet) {
        this.buyerWallet = buyerWallet;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getRiskFilter() {
        return riskFilter;
    }

    public void setRiskFilter(String riskFilter) {
        this.riskFilter = riskFilter;
    }

    public String getCreditTransactionalFee() {
        return creditTransactionalFee;
    }

    public void setCreditTransactionalFee(String creditTransactionalFee) {
        this.creditTransactionalFee = creditTransactionalFee;
    }

    public String getCreditPromotionalFee() {
        return creditPromotionalFee;
    }

    public void setCreditPromotionalFee(String creditPromotionalFee) {
        this.creditPromotionalFee = creditPromotionalFee;
    }

    public String getCreditTerm() {
        return creditTerm;
    }

    public void setCreditTerm(String creditTerm) {
        this.creditTerm = creditTerm;
    }

    public String getCreditOfferType() {
        return creditOfferType;
    }

    public void setCreditOfferType(String creditOfferType) {
        this.creditOfferType = creditOfferType;
    }

    public String getOriginalInvoiceId() {
        return originalInvoiceId;
    }

    public void setOriginalInvoiceId(String originalInvoiceId) {
        this.originalInvoiceId = originalInvoiceId;
    }

    public String getPaymentSourceSubtype() {
        return paymentSourceSubtype;
    }

    public void setPaymentSourceSubtype(String paymentSourceSubtype) {
        this.paymentSourceSubtype = paymentSourceSubtype;
    }

    public String getCampaignFee() {
        return campaignFee;
    }

    public void setCampaignFee(String campaignFee) {
        this.campaignFee = campaignFee;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignDiscount() {
        return campaignDiscount;
    }

    public void setCampaignDiscount(String campaignDiscount) {
        this.campaignDiscount = campaignDiscount;
    }

    public String getCampaignDiscountCurrency() {
        return campaignDiscountCurrency;
    }

    public void setCampaignDiscountCurrency(String campaignDiscountCurrency) {
        this.campaignDiscountCurrency = campaignDiscountCurrency;
    }
    //</editor-fold>
}
