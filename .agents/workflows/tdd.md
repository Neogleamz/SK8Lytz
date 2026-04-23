---
description: Apply Test-Driven Development (TDD) for utility functions and logic
---

1. **Write the Test First**: Before modifying any application code, write a unit test (using our project's testing framework) that defines the expected behavior.
2. **Fail State**: Output the test code to the chat and confirm that, in its current state, this test would fail.
3. **Implementation**: Write the actual feature code designed specifically to make that test pass.
4. **Verification**: Using `run_command`, run the test suite and confirm it passes before asking the user for further instructions.
