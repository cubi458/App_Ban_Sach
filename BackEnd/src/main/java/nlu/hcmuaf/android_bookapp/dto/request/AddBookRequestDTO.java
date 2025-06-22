package nlu.hcmuaf.android_bookapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nlu.hcmuaf.android_bookapp.enums.EBookFormat;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddBookRequestDTO {
    private String code;
    private String title;
    private String description;
    private double price;
    private String thumbnail;
    private List<String> bookImages;
    private String author;
    private String size;
    private int numPage;
    private EBookFormat bookFormat;
    private String publishCompanyName;
    private Long discountId;
} 