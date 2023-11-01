package com.booking.member.payments.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApprovalResponseDto {
    private String cid;
    private String aid;
    private String tid;
    private String partner_user_id;
    private String partner_order_id;
    private String payment_method_type;
    private String item_name;
    private int quantity;
    private Amount amount;
    private CardInfo card_info;
    private LocalDateTime created_at;
    private LocalDateTime approved_at;

    @Data
    public static class Amount {
        private int total;
        private int tax_free;
        private int vat;
        private int point;
        private int discount;
        private int green_deposit;
    }

    @Data
    public static class CardInfo {
        private String interest_free_install;
        private String bin;
        private String card_type;
        private String card_mid;
        private String approved_id;
        private String install_month;
        private String purchase_corp;
        private String purchase_corp_code;
        private String issuer_corp;
        private String issuer_corp_code;
        private String kakaopay_purchase_corp;
        private String kakaopay_purchase_corp_code;
        private String kakaopay_issuer_corp;
        private String kakaopay_issuer_corp_code;
    }
}
