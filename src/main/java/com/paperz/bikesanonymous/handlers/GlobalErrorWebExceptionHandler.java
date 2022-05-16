package com.paperz.bikesanonymous.handlers;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
@Slf4j
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

  public GlobalErrorWebExceptionHandler(
      DefaultErrorAttributes defaultErrorAttributes,
      ApplicationContext applicationContext,
      ServerCodecConfigurer serverCodecConfigurer) {
    super(defaultErrorAttributes, new WebProperties.Resources(), applicationContext);
    super.setMessageWriters(serverCodecConfigurer.getWriters());
    super.setMessageReaders(serverCodecConfigurer.getReaders());
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(
      final ErrorAttributes errorAttributes) {
    return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
  }

  private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {
    ErrorAttributeOptions options =
        ErrorAttributeOptions.defaults()
            .including(ErrorAttributeOptions.Include.MESSAGE)
            .including(ErrorAttributeOptions.Include.STACK_TRACE);
    final Map<String, Object> errorPropertiesMap = getErrorAttributes(request, options);

    log.error((String) errorPropertiesMap.get("trace"));
    errorPropertiesMap.remove("trace");

    return ServerResponse.status(HttpStatus.BAD_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(errorPropertiesMap));
  }
}
