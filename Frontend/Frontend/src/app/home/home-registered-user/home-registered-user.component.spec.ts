import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeRegisteredUserComponent } from './home-registered-user.component';

describe('HomeRegisteredUserComponent', () => {
  let component: HomeRegisteredUserComponent;
  let fixture: ComponentFixture<HomeRegisteredUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HomeRegisteredUserComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeRegisteredUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
