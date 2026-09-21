# Android signing

Character Build v1.2.0 and later use one permanent release certificate.

- Alias: `characterbuild`
- Certificate SHA-256: `E2:24:B5:72:40:12:68:C6:37:35:23:B0:87:9F:C0:7D:81:74:2B:ED:D0:56:A0:AC:A4:C4:8A:51:0A:22:32:0C`
- Public certificate: `signing/characterbuild-release-cert.pem`

The private keystore and passwords must never be committed to this public repository.
Every APK intended to update an installed v1.2.0+ build must be signed with the matching private key.
