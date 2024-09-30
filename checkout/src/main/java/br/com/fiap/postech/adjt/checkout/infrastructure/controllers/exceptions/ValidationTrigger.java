package br.com.fiap.postech.adjt.checkout.infrastructure.controllers.exceptions;

import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 * Controla as verificações de entidades que entram na controller.
 **/
@AllArgsConstructor
public class ValidationTrigger {
  BindingResult bindingResult;

  /**
   * Verifica se existe algum erro no bidingResult.
   **/
  public void verify() throws BadRequestException {
    if (bindingResult.hasErrors()) {
      var message = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining());
      throw new BadRequestException(message);
    }
  }
}