package com.bitespeed.backendtask.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Record {
    String email;
    String phoneNumber;
}
