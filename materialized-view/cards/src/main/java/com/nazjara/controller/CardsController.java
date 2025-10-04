package com.nazjara.controller;

import com.nazjara.constant.CardsConstants;
import com.nazjara.dto.CardsDto;
import com.nazjara.dto.ResponseDto;
import com.nazjara.service.ICardsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
@Slf4j
public class CardsController {

  private final ICardsService iCardsService;

  @PostMapping("/create")
  public ResponseEntity<ResponseDto> createCard(@Valid @RequestParam("mobileNumber")
  @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
  String mobileNumber) {
    iCardsService.createCard(mobileNumber);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
  }

  @GetMapping("/fetch")
  public ResponseEntity<CardsDto> fetchCardDetails(@RequestParam("mobileNumber")
  @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
  String mobileNumber) {
    var cardsDto = iCardsService.fetchCard(mobileNumber);
    return ResponseEntity.status(HttpStatus.OK).body(cardsDto);
  }

  @PutMapping("/update")
  public ResponseEntity<ResponseDto> updateCardDetails(@Valid @RequestBody CardsDto cardsDto) {
    var isUpdated = iCardsService.updateCard(cardsDto);

    if (isUpdated) {
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
    } else {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ResponseDto(CardsConstants.STATUS_500, CardsConstants.MESSAGE_500_UPDATE));
    }
  }

  @PatchMapping("/delete")
  public ResponseEntity<ResponseDto> deleteCardDetails(@RequestParam("cardNumber")
  Long cardNumber) {
    var isDeleted = iCardsService.deleteCard(cardNumber);

    if (isDeleted) {
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
    } else {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ResponseDto(CardsConstants.STATUS_500, CardsConstants.MESSAGE_500_DELETE));
    }
  }

}
