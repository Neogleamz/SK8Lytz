# Add Skate Spot Labels to Live Map

## Goal
The user wants the static "Skate Spots" (Rinks, Shops, Parks) on the Live Near You map to display their names under their marker icons, just like the active Crew Sessions do. Additionally, they requested to see if there are active sessions happening AT those rinks.

## Proposed Changes

### `src/components/crew/CrewLandingMap.tsx`

We will modify the `Marker` rendering loop for `nearbySpots`. 
Instead of just rendering the circular colored icon, we will wrap it in a container that also renders a text bubble below it containing:
1. The spot's `name`
2. An **Active Sessions** counter. 

**How we detect active sessions at a spot:**
We will use an in-memory geospatial proximity check. Since `CrewLandingMap.tsx` receives both `nearbySpots` and `nearbySessions` as props, we can compare their coordinates. If an active session is happening within ~111 meters (`0.001` degrees of latitude/longitude) of a static skate spot, we will count it as happening "at" that spot and render an orange `[1 Live Session]` indicator on the spot's label.

#### [MODIFY] `CrewLandingMap.tsx`
- Refactor the `nearbySpots.map` render block.
- Add coordinate proximity filtering to count overlapping active sessions.
- Add the `<View>` callout label with the spot name and conditional session count.

### `src/components/crew/CrewCreateScreen.tsx` & `src/components/crew/CrewScheduleScreen.tsx`

We will pass the `nearbySpots` context down to the `LocationPicker` to leverage its built-in smart chips and fast-path local search.
- Filter `nearbySpots` from context: `const eligibleSpots = nearbySpots.filter(s => s.facility_type === 'roller_rink' || s.facility_type === 'skatepark');`
- Pass `curatedSpots={eligibleSpots}` to the `<LocationPicker>` component.

## User Review Required
> [!NOTE]
> Are you ready for me to execute this bundled plan (Map Labels + Smart Location Picker), or do you want to adjust the proximity logic (100 meters)?
