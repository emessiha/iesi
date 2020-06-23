package io.metadew.iesi.metadata.definition.template;

import io.metadew.iesi.datatypes.DataType;
import io.metadew.iesi.metadata.definition.Metadata;
import io.metadew.iesi.metadata.definition.template.matcher.Matcher;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Template extends Metadata<TemplateKey> implements DataType {

    private final String name;
    private List<Matcher> matchers;

    @Builder
    public Template(TemplateKey metadataKey, String name, List<Matcher> matchers) {
        super(metadataKey);
        this.name = name;
        this.matchers = matchers;
    }

    @Override
    public String toString() {
        return "{{^template(" + name + "}}";
    }

    public void addMatcher(Matcher matcher) {
        this.matchers.add(matcher);
    }

}
