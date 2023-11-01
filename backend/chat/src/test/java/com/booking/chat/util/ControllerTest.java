package com.booking.chat.util;

import com.booking.chat.chatroom.controller.ChatroomController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureRestDocs
@ActiveProfiles("local")
@WebFluxTest({
    ChatroomController.class
})
public class ControllerTest {

    //setup
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestDocumentationContextProvider restDocumentation;
}
