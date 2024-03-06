import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedManagerComponent } from './need-manager.component';

describe('NeedManagerComponent', () => {
  let component: NeedManagerComponent;
  let fixture: ComponentFixture<NeedManagerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NeedManagerComponent]
    });
    fixture = TestBed.createComponent(NeedManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
