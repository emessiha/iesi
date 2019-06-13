package io.metadew.iesi.server.rest.ressource.component;

import io.metadew.iesi.metadata.definition.Component;
import io.metadew.iesi.server.rest.controller.ComponentsController;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@org.springframework.stereotype.Component
public class ComponentDtoResourceAssembler extends ResourceAssemblerSupport<List<Component>, ComponentDto> {

    private final ModelMapper modelMapper;

    public ComponentDtoResourceAssembler() {
        super(ComponentsController.class, ComponentDto.class);
        this.modelMapper = new ModelMapper();
    }

    @Override
    public ComponentDto toResource(List<Component> components) {
        ComponentDto componentDto = convertToDto(components);
        componentDto.add(linkTo(methodOn(ComponentsController.class)
                .getByName(componentDto.getName()))
                .withSelfRel());
        return componentDto;
    }

    private ComponentDto convertToDto(List<Component> components) {

        ComponentDto connectionByNameDto = modelMapper.map(components.get(0), ComponentDto.class);
//
        return connectionByNameDto;
    }
}