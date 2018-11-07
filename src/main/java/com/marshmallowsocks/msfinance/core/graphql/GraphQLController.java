package com.marshmallowsocks.msfinance.core.graphql;

import com.marshmallowsocks.msfinance.core.accesstoken.AccessTokenService;
import com.marshmallowsocks.msfinance.core.group.GroupService;
import com.marshmallowsocks.msfinance.core.institution.InstitutionService;
import com.marshmallowsocks.msfinance.user.model.User;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.metadata.strategy.query.AnnotatedResolverBuilder;
import io.leangen.graphql.metadata.strategy.value.jackson.JacksonValueMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(path = "/api", consumes = MediaType.APPLICATION_JSON_VALUE)
public class GraphQLController {

    private final GraphQL graphQL;

    @Autowired
    public GraphQLController(AccessTokenService accessTokenService, GroupService groupService, InstitutionService institutionService) {
        GraphQLSchema schema = new GraphQLSchemaGenerator()
                .withResolverBuilders(
                        //Resolve by annotations
                        new AnnotatedResolverBuilder())
                .withOperationsFromSingleton(accessTokenService)
                .withOperationsFromSingleton(groupService)
                .withOperationsFromSingleton(institutionService)
                .withValueMapperFactory(new JacksonValueMapperFactory())
                .generate();
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    @PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> graphql(@RequestBody Map<String, String> request, @AuthenticationPrincipal final User user) {
        ExecutionResult executionResult = graphQL.execute(ExecutionInput.newExecutionInput()
                .query(request.get("query"))
                .operationName(request.get("operationName"))
                .context(user.getId())
                .build());
        return executionResult.toSpecification();
    }
}
