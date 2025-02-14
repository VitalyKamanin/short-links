package org.example;


import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

class LinkData {
    final String longUrl;
    final LocalDateTime expiryTime;
    int clicksRemaining;
    final UUID userId;

    public LinkData(String longUrl, LocalDateTime expiryTime, int clicksRemaining, UUID userId) {
        this.longUrl = longUrl;
        this.expiryTime = expiryTime;
        this.clicksRemaining = clicksRemaining;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LinkData linkData = (LinkData) o;
        return clicksRemaining == linkData.clicksRemaining && Objects.equals(longUrl, linkData.longUrl) &&
                Objects.equals(expiryTime, linkData.expiryTime) && Objects.equals(userId, linkData.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longUrl, expiryTime, clicksRemaining, userId);
    }
}
