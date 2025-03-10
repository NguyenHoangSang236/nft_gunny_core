package com.nftgunny.core.entities.database;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document("system_config")
public class SystemConfig extends MongoDbEntity implements Serializable {
    @Id
    String id;

    @Field(name = "name")
    @Indexed(unique = true)
    String name;

    @Field(name = "value")
    String value;

    @Field(name = "description")
    String description;
}
