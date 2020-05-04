package io.metadew.iesi.server.rest.resource.component.resource;


import io.metadew.iesi.metadata.definition.component.Component;
import io.metadew.iesi.server.rest.controller.ComponentsController;
import io.metadew.iesi.server.rest.resource.component.dto.ComponentByNameDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@org.springframework.stereotype.Component
public class ComponentGetByNameDtoAssembler extends RepresentationModelAssemblerSupport<List<Component>, ComponentByNameDto> {

    public ComponentGetByNameDtoAssembler() {
        super(ComponentsController.class, ComponentByNameDto.class);
    }

    @Override
    public ComponentByNameDto toModel(List<Component> components) {
        if (components.isEmpty()) {
            return null;
        } else {
            ComponentByNameDto componentDto = convertToDto(components);
            Link versionLink = linkTo(methodOn(ComponentsController.class).get(components.get(0).getName(), components.get(0).getVersion().getMetadataKey().getComponentKey().getVersionNumber()))
                    .withRel("version: " + componentDto.getVersions().get(0));
            componentDto.add(linkTo(methodOn(ComponentsController.class)
                    .getByName(componentDto.getName()))
                    .withRel("component:" + componentDto.getName()));
            componentDto.add(versionLink);
            return componentDto;
        }
    }

    private ComponentByNameDto convertToDto(List<Component> components) {
        return new ComponentByNameDto(components.get(0).getName(), components.get(0).getType(), components.get(0).getDescription(),
                components.stream().map(component -> component.getVersion().getMetadataKey().getComponentKey().getVersionNumber()).collect(Collectors.toList()));

    }
}
