package net.foodeals.support.application.dtos;

import java.util.UUID;

public record UserLite(Integer id, String name, String email, String phone ,String avatar,String type) {}