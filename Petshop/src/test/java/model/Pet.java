package model;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class Pet {
    private int id;
    private PetCategory category;
    private String name;
    private List<Object> photoUrls;
    private List<Object> tags;
    private PetStatus status;
}
