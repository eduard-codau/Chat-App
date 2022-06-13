import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomepageUnregisterUserComponent } from './homepage-unregister-user.component';

describe('HomepageUnregisterUserComponent', () => {
  let component: HomepageUnregisterUserComponent;
  let fixture: ComponentFixture<HomepageUnregisterUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HomepageUnregisterUserComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HomepageUnregisterUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
