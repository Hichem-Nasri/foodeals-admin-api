package net.foodeals.support.application.dtos;

import java.util.List;
import java.util.UUID;

public record SupportDetailDto(
	    UUID id,
	    String subject,
	    String message,
	    String status,
	    String priority,
	    String category,
	    UserDetail user,
	    String created_at,
	    String updated_at,
	    UserLite assigned_to,
	    List<String> tags,
	    List<AttachmentDto> attachments,
	    List<ResponseDto> responses
	) {
	    public record UserDetail(Integer id, String name, String email, String phone, String avatar, String account_created) {}
	    public record AttachmentDto(String id, String filename, String url, Long size, String type) {}
	    public record ResponseDto(String id, String message, String type, Author author, String created_at, boolean is_internal) {
	        public record Author(Integer id, String name, String email, String role) {}
	    }
	}