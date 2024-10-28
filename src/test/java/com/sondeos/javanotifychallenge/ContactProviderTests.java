package com.sondeos.javanotifychallenge;

import com.sondeos.javanotifychallenge.providers.ContactProvider;
import com.sondeos.javanotifychallenge.providers.dto.ContactDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ContactProviderTests {

    @Autowired
    ContactProvider contactProvider;

    @Test
    void getExistingContact() {
        String id = "1";
        ContactDto contact = contactProvider.getContact(id);
        System.out.println(contact.toString());
        assertEquals(id, contact.getId());
    }

    @Test
    void getNonExistingContact() {
        //assert throw exception HttpClientErrorException 404 Not Found
        String id = "100";
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            contactProvider.getContact(id);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }


}
