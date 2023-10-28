package com.dreamtech.tldental.services;
import com.dreamtech.tldental.models.EmailDetails;

public interface EmailService {
	String sendSimpleMail(EmailDetails details);
}
