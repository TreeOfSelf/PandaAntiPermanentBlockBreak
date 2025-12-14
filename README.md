![image](https://github.com/user-attachments/assets/22527936-8f62-4c17-835f-6859bdf85fe8)

# PandaAntiPermanentBlockBreak

## Description

Prevents breaking permanent blocks via piston head glitches and nether portal generation. Includes granular configuration for protecting specific block types.

## Configuration

`config/PandaAntiPermanentBlockBreak.json` generated at runtime:
```json
{
  "protectBedrock": true,
  "protectEndPortal": true,
  "protectEndPortalFrame": true,
  "protectEndGateway": true,
  "strictBreakCheck": false
}
```

**Options:**
- `protectBedrock` - Prevents bedrock breaking
- `protectEndPortal` - Prevents end portal block breaking
- `protectEndPortalFrame` - Prevents end portal frame breaking
- `protectEndGateway` - Prevents end gateway breaking
- `strictBreakCheck` - If `true`, makes protected blocks impossible to break by any method (commands, creative, etc.). Exception: bedrock that breaks when spawning the ender dragon

## Try it out
- `hardcoreanarchy.gay` (Deathban Anarchy)
- `sky.hardcoreanarchy.gay` (Skyblock Anarchy)

## Support

[Discord](https://discord.gg/3tP3Tqu983)

## License

[CC0](https://creativecommons.org/public-domain/cc0/)