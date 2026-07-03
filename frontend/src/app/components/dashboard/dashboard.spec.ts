import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DashboardComponent } from './dashboard'; // <-- Updated import

describe('DashboardComponent', () => {
  let component: DashboardComponent; // <-- Updated type
  let fixture: ComponentFixture<DashboardComponent>; // <-- Updated type

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardComponent] // <-- Updated import
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardComponent); // <-- Updated component
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});