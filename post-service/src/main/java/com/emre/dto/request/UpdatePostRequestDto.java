package com.emre.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequestDto {
    private String id;
    private String content;
    private List<String> addMediaUrls = new ArrayList<>();
    private List<String> removeMediaUrls = new ArrayList<>();
}
