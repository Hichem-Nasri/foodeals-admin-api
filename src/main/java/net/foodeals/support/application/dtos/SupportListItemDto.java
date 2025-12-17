package net.foodeals.support.application.dtos;

import java.util.List;
import java.util.UUID;

import net.foodeals.support.application.dtos.SupportDetailDto.AttachmentDto;

public record SupportListItemDto(
 UUID id,
 String subject,
 String status,
 String priority,
 UserLite user,
 String category,
 String created_at,
 String updated_at,
 String last_response_at,
 Integer response_count,
 UserLite assigned_to,
 List<AttachmentDto> attachments,
 List<String> tags
) {}