package uj.pwkp.gr1.vet.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.LogoutRequest;
import uj.pwkp.gr1.vet.VetApp.entity.TokenBlackList;
import uj.pwkp.gr1.vet.VetApp.exception.VetAppResourceType;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.BadRequestVetAppException;
import uj.pwkp.gr1.vet.VetApp.repository.TokenBlackListRepository;

@RestController
@RequestMapping("/logout")
public class LogoutRestController {
    private final TokenBlackListRepository tokenBlackListRepository;

    @Autowired
    public LogoutRestController(TokenBlackListRepository tokenBlackListRepository) {
        this.tokenBlackListRepository = tokenBlackListRepository;
    }

    @PostMapping
    public String logout(@RequestBody LogoutRequest logoutRequest) {
        TokenBlackList token;
        try {
            token = tokenBlackListRepository.save(
                    TokenBlackList.builder()
                            .token(logoutRequest.getToken())
                            .build());
        } catch (Exception e) {
            throw new BadRequestVetAppException("Such token doesn't exists.", VetAppResourceType.USER);
        }
        return token.toString();
    }
}
