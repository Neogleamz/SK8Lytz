export const logFatalCrash = async (error: Error, stack: string) => {
  console.error("Fatal Crash", error, stack);
  return 'dummy-event-id';
};
