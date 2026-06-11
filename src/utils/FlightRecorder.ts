export interface Breadcrumb {
  timestamp: string;
  category: 'NAVIGATION' | 'ACTION' | 'BLE' | 'NETWORK' | 'ERROR' | 'INFO';
  message: string;
  data?: unknown;
}

class FlightRecorderInstance {
  private readonly MAX_BREADCRUMBS = 50;
  private breadcrumbs: Breadcrumb[] = [];

  leaveBreadcrumb(category: Breadcrumb['category'], message: string, data?: unknown) {
    this.breadcrumbs.push({
      timestamp: new Date().toISOString(),
      category,
      message,
      data
    });

    if (this.breadcrumbs.length > this.MAX_BREADCRUMBS) {
      this.breadcrumbs.shift(); // Remove oldest
    }
  }

  getBreadcrumbs(): Breadcrumb[] {
    return [...this.breadcrumbs];
  }

  clear() {
    this.breadcrumbs = [];
  }
}

export const FlightRecorder = new FlightRecorderInstance();
