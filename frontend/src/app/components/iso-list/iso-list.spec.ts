import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IsoList } from './iso-list';

describe('IsoList', () => {
  let component: IsoList;
  let fixture: ComponentFixture<IsoList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IsoList],
    }).compileComponents();

    fixture = TestBed.createComponent(IsoList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
