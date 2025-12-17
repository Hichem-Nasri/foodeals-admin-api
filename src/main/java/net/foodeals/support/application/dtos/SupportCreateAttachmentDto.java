package net.foodeals.support.application.dtos;

public record SupportCreateAttachmentDto(
	    String filename,
	    String url,       // URL déjà hébergée (CDN, S3, etc.)
	    Long size,
	    String type       // mime type (ex: "image/png", "application/pdf")
	) {}