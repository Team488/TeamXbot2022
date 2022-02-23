# Climber states

## States

### Arm target positions

* FullyRetracted
* ClearCurrentBar
* FullyExtended
* EngageNextBar

### Pivot states

* PivotIn
* PivotOut

### Latch states

* Arm
* Release

## Flow diagram

```mermaid
stateDiagram-v2
    moving: <b>Moving to bars</b> <br> Arm- FullyRetracted <br> Pivot- PivotIn <br> Latch- Release
    grounded: <b>In position</b>
    initial_extend: <b>Extend arms</b> <br> Arm- FullyExtended
    move_to_engage: <b>Move to let hooks engage</b>
    initial_extend_verify: <b>Engage first bar</b> <br> Arm- EngageNextBar
    first_lift: <b>Lift to bar</b> <br> Arm- FullyRetracted <br> Pivot- PivotIn <br> Latch- Arm

    [*] --> moving
    moving --> grounded

    %% We are positioned at the monkey bars at this point
    grounded --> initial_extend
    initial_extend --> move_to_engage
    move_to_engage --> initial_extend_verify
    initial_extend_verify --> initial_extend : If didn't engage properly

    %% We're on the first bar, time to pull up!
    initial_extend_verify --> first_lift : If hooks properly engaged
    first_lift --> Climbing

    %% Climbing cycle
    state Climbing {
        lift: <b>Lift to bar</b> <br> Arm- FullyRetracted <br> Pivot- PivotIn <br> Latch- Arm
        test_latch: <b>Test latch</b> <br> Arm- ClearCurrentBar <br> Pivot- PivotIn <br> Latch- Arm
        pivot: <b>Pivot arm</b> <br> Arm- ClearCurrentBar <br> Pivot- PivotOut <br> Latch- Arm
        extend: <b>Extend arm</b> <br> Arm- FullyExtended <br> Pivot- PivotOut <br> Latch- Arm
        unpivot: <b>Pivot arm in</b> <br> Arm- FullyExtended <br> Pivot- PivotIn <br> Latch- Arm
        engage_bar: <b>Engage next bar</b> <br> Arm- EngageNextBar <br> Pivot- PivotIn <br> Latch- Arm
        unlatch: <b>Unlatch ⚠</b> <br> Arm- EngageNextBar <br> Pivot- PivotIn <br> Latch- Release
        fast_pivot: <b>Pivot to reduce impact</b> <br> Arm- EngageNextBar <br> Pivot- PivotOut <br> Latch- Release
        disaster: 🔥🚒🧯🚑🏥

        [*] --> lift
        lift --> test_latch
        test_latch --> lift : If robot not latched
        test_latch --> pivot : If robot latched
        pivot --> extend
        extend --> unpivot
        unpivot --> engage_bar
        engage_bar --> unpivot : If hooks fail

        note right of unlatch : Make sure that both hooks are engaged!!
        engage_bar --> unlatch : If both hooks engaged
        unlatch --> fast_pivot : Very quickly after unlatch
        fast_pivot --> lift : If there is another bar to climb to
        unlatch --> disaster : If hooks were not engaged
        fast_pivot --> [*] : If on last bar

    }
```