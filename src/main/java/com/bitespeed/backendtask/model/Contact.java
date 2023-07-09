package com.bitespeed.backendtask.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Contact {
    int primaryContactId;
    List<String> emails;
    List<String> phoneNumbers;
    List<Integer> secondaryContactIds;
}
