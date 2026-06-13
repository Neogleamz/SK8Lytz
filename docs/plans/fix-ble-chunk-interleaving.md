# BLE Chunk Interleaving 

## Objective
Convert the BLE payload writing engine from naive parallel execution (`Promise.all()`) to Synchronous Interleaved Chunking.

## Background
Currently, when a large payload (like a "Pro Effect" / Custom Mode) is dispatched to a Group of skates, `Promise.all` attempts to push complete payloads to both skates simultaneously. Android's Baseband radio (GATT queue) cannot multithread connections to multiple peripherals and corrupts its internal buffer, resulting in a permanent Promise lockup that freezes the app's `writeMutex`.

## Proposed Strategy
1. **Remove `Promise.all` over targets**: In `useBLE.ts -> writeToDevice`, stop parallelizing the transmission of the entire chunk array.
2. **Implement Interleaving Logic**:
    - Calculate max number of chunks across all targeted devices.
    - Loop through chunk indexes `for (let i = 0; i < maxChunks; i++)`.
    - Inside that loop, loop through targets `for (const device of targets)`.
    - Deliver exactly `Chunk i` to the device.
    - Sleep 5ms at the bottom of the chunk loop.
3. **Outcome:** Android receives perfect, sequential single-file GATT instructions, avoiding Buffer Overflow. Visually, the skates receive the animations frame-for-frame simultaneously.

## Risk Assessment
- **Risk Level:** H-RISK (Modifies core BLE write engine)
- **Impact:** High (Eliminates Pro Effect lockups and Mutex freezing)
- **Complexity:** Meal (Logic inversion of a highly asynchronous nested loop).
