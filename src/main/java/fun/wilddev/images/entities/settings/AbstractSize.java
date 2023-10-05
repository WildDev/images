package fun.wilddev.images.entities.settings;

import fun.wilddev.images.vars.Chars;

import org.springframework.data.annotation.Id;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.*;

@Setter
@Getter
public abstract class AbstractSize {

    @Id
    protected String id;

    @Field
    protected Integer width;

    @Field
    protected Integer height;

    protected String getValueOrDefault(Number number) {
        return (number == null ? Chars.QUESTION_MARK : number).toString();
    }

    @Override
    public String toString() {
        return getValueOrDefault(width) + 'x' + getValueOrDefault(height);
    }
}
