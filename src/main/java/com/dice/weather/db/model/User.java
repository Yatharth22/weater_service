package com.dice.weather.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.dice.weather.model.Constants.CollectionName.USER;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(USER)
@CompoundIndex(name = "idx_1", def = "{'user_name' : 1}", unique = true)
public class User {

    @Id
    private String id;

    @Field("user_name")
    @JsonProperty("user_name")
    private String userName;

    @Field("password")
    @JsonProperty("password")
    private String password;

    @Field("mobile")
    @JsonProperty("mobile")
    private String mobile;

    @CreatedDate
    @JsonProperty("created_at")
    @Field("created_at")
    private Long createdAt;

    @LastModifiedDate
    @Field("updated_at")
    @JsonProperty("updated_at")
    private Long updatedAt;
}
