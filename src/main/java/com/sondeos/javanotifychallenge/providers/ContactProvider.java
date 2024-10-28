package com.sondeos.javanotifychallenge.providers;

import com.sondeos.javanotifychallenge.providers.dto.ContactDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/* Esta clase y sus métodos pueden ser modificados si se requiere */

@Component
public class ContactProvider {

    public ContactDto getContact(String id){

        RestClient httpClient = RestClient.create();

        String url = "http://notify.showvlad.com/api/contact/"+id;

        ContactDto contact = httpClient.get()
                .uri(url)
                .retrieve()
                .body(ContactDto.class);

        return contact;
    }




}
