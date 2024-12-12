package com.word_training.api.model.queries;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class RecordPageInputQuery {

   private String text;
   private List<String> typeIn;
   private Boolean pending;
}
