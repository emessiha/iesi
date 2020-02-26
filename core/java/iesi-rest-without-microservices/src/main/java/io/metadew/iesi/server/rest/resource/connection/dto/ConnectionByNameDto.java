package io.metadew.iesi.server.rest.resource.connection.dto;

import io.metadew.iesi.server.rest.resource.Dto;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ConnectionByNameDto extends Dto {

    private String name;
    private String type;
    private String description;
    private List<String> environments;
}
