# Music Mode: Mic & Play Buttons Scaling Fix (fix/music-buttons-scaling)

### Design Decisions & Rationale

To dynamically fill the available space without overlapping, we need to abandon hardcoded pixel widths on the microphone footprint and lean into React Native's Flexbox engine. By assigning `flex: 1` to the microphone touch targets and expanding the play button footprint proportionally, the components will fluidly scale and center themselves perfectly on both small mobile layouts and wider screens.

## Proposed Changes

### `DockedController.tsx`

We will rewrite the StyleSheet objects controlling the Music Mode footer area.

- **MODIFY** `styles.micControlSection`: Adjust `justifyContent` to `'space-between'`, enforce `width: '100%'`, and widen the padding.
- **MODIFY** `styles.micIconBtn`: Remove the fixed `width: 90` and replace it with `flex: 1`. This allows the APP MIC and DEVICE MIC buttons to automatically stretch and consume all available space flanking the center play button evenly.
- **MODIFY** `styles.playButtonMain`: Currently hardcoded at 44x44. In order to "fill available space and scale", we will slightly increase the fixed footprint to `52x52` (or `60x60`) and apply a more generous `marginHorizontal` to push the microphones outwards, creating a balanced, non-overlapping design regardless of the phone's CSS width footprint.

> [!IMPORTANT]  
> **User Review Required**  
> Review the strategy above! I will strip the fixed widths and replace them with responsive flex units. Reply with **"proceed"** to execute the styling changes!
