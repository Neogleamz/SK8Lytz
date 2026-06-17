export type ViewState = 'idle' | 'loading' | 'error' | 'empty' | 'success';

export interface ViewStateInfo {
  viewState: ViewState;
  errorMsg: string;
}
