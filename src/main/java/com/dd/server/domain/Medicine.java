package com.dd.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "medicine")
@Getter
@Setter
public class Medicine {

    @Id
    @Column(name = "item_seq", nullable = false, unique = true)
    private String itemSeq;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "entp_seq")
    private String entpSeq;

    @Column(name = "entp_name")
    private String entpName;

    @Column(name = "chart")
    private String chart;

    @Column(name = "item_image")
    private String itemImage;

    @Column(name = "print_front")
    private String printFront;

    @Column(name = "print_back")
    private String printBack;

    @Column(name = "drug_shape")
    private String drugShape;

    @Column(name = "color_class1")
    private String colorClass1;

    @Column(name = "color_class2")
    private String colorClass2;

    @Column(name = "line_front")
    private String lineFront;

    @Column(name = "line_back")
    private String lineBack;

    @Column(name = "leng_long")
    private String lengLong;

    @Column(name = "leng_short")
    private String lengShort;

    @Column(name = "thick")
    private String thick;

    @Column(name = "img_regist_ts")
    private String imgRegistTs;

    @Column(name = "class_no")
    private String classNo;

    @Column(name = "class_name")
    private String className;

    @Column(name = "etc_otc_name")
    private String etcOtcName;

    @Column(name = "item_permit_date")
    private String itemPermitDate;

    @Column(name = "form_code_name")
    private String formCodeName;

    @Column(name = "mark_code_front_anal")
    private String markCodeFrontAnal;

    @Column(name = "mark_code_back_anal")
    private String markCodeBackAnal;

    @Column(name = "mark_code_front_img")
    private String markCodeFrontImg;

    @Column(name = "mark_code_back_img")
    private String markCodeBackImg;

    @Column(name = "item_eng_name")
    private String itemEngName;

    @Column(name = "change_date")
    private String changeDate;

    @Column(name = "mark_code_front")
    private String markCodeFront;

    @Column(name = "mark_code_back")
    private String markCodeBack;

    @Column(name = "edi_code")
    private String ediCode;

    @Column(name = "bizrno")
    private String bizrno;
}
